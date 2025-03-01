require recipes-bsp/u-boot/u-boot.inc

inherit fsl-u-boot-localversion

SUMMARY = "U-boot provided by NXP"
LICENSE = "GPL-2.0-only & BSD-3-Clause & BSD-2-Clause & LGPL-2.0-only & LGPL-2.1-only"
LIC_FILES_CHKSUM = " \
    file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://Licenses/bsd-2-clause.txt;md5=6a31f076f5773aabd8ff86191ad6fdd5 \
    file://Licenses/bsd-3-clause.txt;md5=4a1190eac56a9db675d58ebe86eaf50c \
    file://Licenses/lgpl-2.1.txt;md5=4fbd65380cdd255951079008b364516c \
"

DEPENDS:append = " libgcc dtc-native bison-native bc-native"
DEPENDS:append:qoriq-ppc = " boot-format-native"

PROVIDES = "virtual/bootloader u-boot"

SCMVERSION = "y"
LOCALVERSION = ""

HSE_SUPPORT = "${@bb.utils.contains('DISTRO_FEATURES', 'hse', 'recipes-bsp/u-boot/u-boot-nxp-hse.inc', '', d)}"
include ${HSE_SUPPORT}

VERIFIEDBOOT_SUPPORT = "${@bb.utils.contains('DISTRO_FEATURES', 'verifiedboot', 'recipes-bsp/u-boot/u-boot-nxp-verifiedboot.inc', '', d)}"
include ${VERIFIEDBOOT_SUPPORT}

DELTA_UBOOT_DEFCONFIG ?= ""

# Enable Xen default boot if Xen enabled
DELTA_UBOOT_DEFCONFIG:append:s32 = " ${@bb.utils.contains('DISTRO_FEATURES', 'xen', ' xen_config.cfg', '', d)}"
SRC_URI:append:s32 = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'file://build/xen_config.cfg', '', d)} \
"

# Enable OP-TEE
DELTA_UBOOT_DEFCONFIG:append:s32cc = "${@bb.utils.contains('DISTRO_FEATURES', 'optee', ' optee_config.cfg', '', d)}"
SRC_URI:append:s32cc = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'optee', 'file://build/optee_config.cfg', '', d)} \
"

# Switch the UART baudrate to be used if it is the case
S32_BAUDRATE = "${@d.getVar("SERIAL_CONSOLES", True).split(" ")[0].split(";")[0] }"
DELTA_UBOOT_DEFCONFIG:append:s32cc = " serial_configs/baudrate_${S32_BAUDRATE}.cfg"
SRC_URI:append:s32cc = " \
    ${@ 'file://build/serial_configs/baudrate_${S32_BAUDRATE}.cfg' } \
"

# FIXME: Allow linking of 'tools' binaries with native libraries
#        used for generating the boot logo and other tools used
#        during the build process.
EXTRA_OEMAKE += 'HOSTCC="${BUILD_CC} ${BUILD_CPPFLAGS}" \
                 HOSTLDFLAGS="${BUILD_LDFLAGS}" \
                 HOSTSTRIP=true'

PACKAGE_ARCH = "${MACHINE_ARCH}"

USRC ?= ""
S = '${@oe.utils.conditional("USRC", "", "${WORKDIR}/git", "${USRC}", d)}'
B = "${WORKDIR}/build"

do_merge_delta_config[dirs] = "${B}"
do_merge_delta_config() {
    # add config fragments
    echo ${UBOOT_MACHINE}
    for config in ${UBOOT_MACHINE}; do
        # replace <config-type>_config to <config-type>_defconfig to
        # match the config name file
        config="$(echo "${config}" | sed -e 's/'_config'/'_defconfig'/g')"

        cp ${S}/configs/${config} .config
        for deltacfg in ${DELTA_UBOOT_DEFCONFIG}; do
            ${S}/scripts/kconfig/merge_config.sh -m .config ${deltacfg}
        done
        cp -vf ${B}/.config ${S}/configs/${config}
    done
}
addtask merge_delta_config before do_configure after do_patch

ENV_STAGE_DIR = "${datadir}/env"

do_install:append:s32() {
    unset i j
    mkdir -p ${D}${ENV_STAGE_DIR}
    # we should have one config
    for config in ${UBOOT_MACHINE}; do
        i=`expr $i + 1`;
        unset j
        for type in ${UBOOT_CONFIG}; do
            j=`expr $j + 1`;
            if [ $j -ne $i ]; then
                continue
            fi

            # remove any empty lines which might break the environment
            sed '/^[[:space:]]*$/d' -i ${B}/${config}/env-default.txt
            # install our environment file to usr/share to have it staged by yocto
            install ${B}/${config}/env-default.txt ${D}${ENV_STAGE_DIR}/u-boot-default-flashenv${type}.txt
            install -d ${DEPLOY_DIR_IMAGE}
            install -v ${B}/${config}/u-boot-nodtb.bin ${DEPLOY_DIR_IMAGE}/u-boot-nodtb.bin-${type}
            install -d ${DEPLOY_DIR_IMAGE}/tools
            install -v ${B}/${config}/u-boot-s32.cfgout ${DEPLOY_DIR_IMAGE}/tools/u-boot-s32.cfgout-${type}
            find ${B}/${config}/ -name "qspi_params_*" -exec install -v {} ${DEPLOY_DIR_IMAGE}/tools/ \;
        done
    done

}

python do_clean:append () {
    """clear the custom added files"""

    mkimage = os.path.join(d.getVar("DEPLOY_DIR_IMAGE"), "/tools/mkimage")
    oe.path.remove(mkimage)
}

FILES:${PN} += "${ENV_STAGE_DIR}"

COMPATIBLE_MACHINE = "s32|qoriq"
