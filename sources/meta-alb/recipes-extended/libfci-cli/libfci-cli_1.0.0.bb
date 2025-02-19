DESCRIPTION = "LibFCI Example: Command line tool for configuration of PFE"
HOMEPAGE = "https://github.com/nxp-auto-linux/pfeng"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE-BSD3.txt;md5=a76ce5ba347a9a89c4a886e042668dbb"

PR = "r0"

URL ?= "git://github.com/nxp-auto-linux/pfeng;protocol=https;nobranch=1"
SRC_URI = "${URL}"

# Building kernel version specific PFE driver support should be reverted
# once the same version of the PFE driver has been validated for both
# kernel versions (primary and secondary)
KERNEL_MAJ_VER = "${@oe.utils.trim_version("${PREFERRED_VERSION_linux-s32}", 2)}"
SRCREV ?= "${@oe.utils.ifelse(d.getVar('KERNEL_MAJ_VER') == '6.6', 'fdafef25bce0a9676d2915e836219b832ce3dc64', '9ef7ac3ed8d80be28da637d982f336c4239aeb9a')}"

S = "${WORKDIR}/git"
MDIR = "${S}/sw/libfci_cli"

# Workaround for makefile.
# The makefile is unfortunately not properly prepared to be ran by YOCTO (no option to provide sysroot for toolchain).
# Therefore, the sysroot is prepended to "CCFLAGS_all" for compiler, and to "LDFLAGS_all" for linker.
# Those symbols are recognized by the makefile and should not collide with YOCTO symbols.
CCFLAGS_all = "--sysroot=\"${STAGING_DIR_HOST}\""
LDFLAGS_all = "--sysroot=\"${STAGING_DIR_HOST}\""
SYSROOT_WORKAROUND = "CCFLAGS_all=${CCFLAGS_all} LDFLAGS_all=${LDFLAGS_all}"

CFLAGS:prepend = "-I${S} "

PACKAGES = "${PN} ${PN}-dbg"
FILES:${PN} += "${bindir}/libfci_cli"

RDEPENDS:${PN} = "pfe"
RDEPENDS:${PN}-dbg = "pfe"

do_compile() {
	cd ${MDIR}
	${SYSROOT_WORKAROUND} ${MAKE} TARGET_OS=LINUX PLATFORM=${TARGET_SYS} all
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${MDIR}/libfci_cli ${D}${bindir}
}

COMPATIBLE_MACHINE = "s32g"
