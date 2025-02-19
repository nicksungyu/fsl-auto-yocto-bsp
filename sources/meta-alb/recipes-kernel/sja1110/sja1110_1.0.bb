# Copyright 2020 NXP

SUMMARY = "SJA1110 SPI Driver"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit module

SRC_URI = "git://github.com/nxp-archive/autoivnsw_sja1110_linux;branch=master;protocol=https"
SRC_URI += "\
	file://0001-sja1110_init-fix-various-issues.patch \
	file://0001-sja1110-Add-Linux-kernel-v6.1-rt-compatibility-patch.patch \
"
SRCREV = "ac06f130d512e75edcf98d1a8304edd90055e525"

S = "${WORKDIR}/git"

EXTRA_OEMAKE:append = " KERNELDIR=${KBUILD_OUTPUT} TARGET_ARCH=${ARCH} LOCAL_TOOLCHAIN=${CROSS_COMPILE} LOCAL_COMPILER=${CROSS_COMPILE}gcc"

SJA_LIBDIR = "${base_libdir}"
SJA_MODDIR = "${sysconfdir}/modules-load.d"

SJA1110_UC_FW ?= ""
SJA1110_SWITCH_FW ?= ""

module_do_install:append() {
	install -d ${D}/${SJA_LIBDIR}/firmware
	if [ -f "${SJA1110_UC_FW}" ]; then
		cp -f ${SJA1110_UC_FW} ${D}/${SJA_LIBDIR}/firmware/sja1110_uc.bin
	fi
	if [ -f "${SJA1110_SWITCH_FW}" ]; then
		cp -f ${SJA1110_SWITCH_FW} ${D}/${SJA_LIBDIR}/firmware/sja1110_switch.bin
	fi
}

KERNEL_MODULE_AUTOLOAD += "sja1110"

FILES:${PN} += "${SJA_LIBDIR}/*"
FILES:${PN} += "${SJA_MODDIR}/*"

PROVIDES = "kernel-module-sja1110${KERNEL_MODULE_PACKAGE_SUFFIX}"
RPROVIDES:${PN} = "kernel-module-sja1110${KERNEL_MODULE_PACKAGE_SUFFIX}"

COMPATIBLE_MACHINE = "(s32g274ardb2|s32g399ardb3)"
