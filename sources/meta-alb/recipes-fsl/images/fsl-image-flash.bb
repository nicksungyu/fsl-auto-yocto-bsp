#
# Copyright 2018-2019 NXP
# Generic recipe to create a boot flash image
# We also want to permit flash image generation.
# We are using the new flashimage class to get a full flash image
#
# <Heinz.Wrobel@nxp.com>
#

# We *only* want to build a basic flashimage, nothing else.
# Yocto requires us to set IMAGE_FSTYPES prior to inheriting the
# image class.
IMAGE_FSTYPES = "flashimage"

require fsl-image-emptyrootfs.inc
require ${@bb.utils.contains('DISTRO_FEATURES', 'pfe', 'recipes-fsl/images/fsl-image-pfe.inc', '', d)}

do_prepare_recipe_sysroot[depends] += "${@bb.utils.contains('DISTRO_FEATURES', 'verifiedboot', 'fsl-image-itb:do_mkimage', '', d)}"

# Userspace support for QSPI Flash under Linux for S32CC platforms
IMAGE_INSTALL:append:s32cc = " mtd-utils "
