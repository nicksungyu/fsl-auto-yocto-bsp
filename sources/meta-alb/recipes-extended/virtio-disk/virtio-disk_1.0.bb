# Copyright 2023 NXP
# 
# Recipe for standalone virtio-disk backend for xen

DESCRIPTION = "virtio-disk"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"
DEPENDS = "xen xen-tools"

S = "${WORKDIR}/git"

SRCREV = "ca6ee0a49c5efabfb704691f0a8e88d86f93969e"

SRC_URI[sha256sum] = "1770023f0c5c2f2dac6fded0d1784d5aeb7d7817b347a1014b296434bdb9a6ee"

SRC_URI:append = " \
    git://github.com/xen-troops/virtio-disk.git;protocol=https;branch=virtio_next \
" 

do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/virtio-disk ${D}${bindir}/virtio-disk
}

FILES:${PN} += " \
    ${bindir}/virtio-disk \
"