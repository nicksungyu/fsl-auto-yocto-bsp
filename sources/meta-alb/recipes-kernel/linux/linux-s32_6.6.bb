PV = "6.6.12"

require recipes-kernel/linux/linux-s32.inc

SRCREV = "649905a72693323b6e5060896cf4830a49d6bc9a"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

PV_MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"
