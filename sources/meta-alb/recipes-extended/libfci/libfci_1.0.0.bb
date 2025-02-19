DESCRIPTION = "libFCI networking acceleration library"
HOMEPAGE = "https://github.com/nxp-auto-linux/pfeng"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE-GPL2.txt;md5=5dcdfe25f21119aa5435eab9d0256af7"

PR = "r0"

URL ?= "git://github.com/nxp-auto-linux/pfeng;protocol=https;nobranch=1"
SRC_URI = "${URL}"

# Building kernel version specific PFE driver support should be reverted
# once the same version of the PFE driver has been validated for both
# kernel versions (primary and secondary)
KERNEL_MAJ_VER = "${@oe.utils.trim_version("${PREFERRED_VERSION_linux-s32}", 2)}"
SRCREV ?= "${@oe.utils.ifelse(d.getVar('KERNEL_MAJ_VER') == '6.6', 'fdafef25bce0a9676d2915e836219b832ce3dc64', '9ef7ac3ed8d80be28da637d982f336c4239aeb9a')}"

S = "${WORKDIR}/git"
MDIR = "${S}/sw/xfci/libfci"
LIBBUILDDIR = "${S}/sw/xfci/libfci/build/${TARGET_SYS}-release"

CFLAGS:prepend = "-I${S} "

PACKAGES = "${PN}-staticdev"

RDEPENDS:${PN}-staticdev = "pfe"

do_compile() {
	cd ${MDIR}
	${MAKE} TARGET_OS=LINUX PLATFORM=${TARGET_SYS} ARCH=${PACKAGE_ARCH}  linux
}

do_install() {
	install -d ${D}${libdir}
	install -m 0644 ${LIBBUILDDIR}/libfci.a ${D}${libdir}
}

COMPATIBLE_MACHINE = "s32g"
