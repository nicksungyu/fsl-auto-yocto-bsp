# 'demo-virt-eth' recipe is not maintained for S32CC platforms.

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://pcie_virt_eth/COPYING;md5=39bba7d2cf0ba1036f2a6e2be52fe3f0"

URL ?= "git://github.com/nxp-auto-linux/alb-demos;protocol=https"
BRANCH ?= "${RELEASE_BASE}"
SRC_URI = "${URL};branch=${BRANCH}"

S = "${WORKDIR}/git"
SRCREV = "9739b2272a9abf3215bd2a539888489c2a72896e"
SAMPLESDIR = "/opt/samples"
DESTDIR = "${D}${SAMPLESDIR}"

do_install() {
        install -d ${DESTDIR}
        oe_runmake install INSTALLDIR=${DESTDIR}
}

FILES:${PN} = "${SAMPLESDIR}"
FILES:${PN}-dbg += "${SAMPLESDIR}/.debug"

DEMO_PCIE_APPS ?= "net_ep net_rc"

DEMO_PCIE_APPS:ls2 = "net_rc"
DEMO_PCIE_APPS:lx2160a = "net_rc"

EXTRA_OEMAKE = "samples=pcie_virt_eth apps="${DEMO_PCIE_APPS}""
