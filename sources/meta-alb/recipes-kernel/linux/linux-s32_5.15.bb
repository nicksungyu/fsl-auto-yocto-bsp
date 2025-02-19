PV = "5.15.145"

require recipes-kernel/linux/linux-s32.inc

SRCREV = "3a3fafb13baa8bee81753ad2f57cfb0a7821572a"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

PV_MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"
