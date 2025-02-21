# fsl-auto-yocto-bsp

* This repo is based on  https://github.com/nxp-auto-linux/auto_yocto_bsp -b release/bsp40.0

### Building the Linux BSP using YOCTO
1. Update the package manager, where <pkg-mgr> is the package manager for your distribution( like **apt-get or apt for Debian/Ubuntu, yum or dnf for CentOS/Fedora** ) :
```
  sudo <pkg-mgr> update
```
2. Install dependencies :
*  python 2.x - 2.6 or newer :
```
  sudo <pkg-mgr> install git
```
* git 1.8.3 or newer :
```
  sudo <pkg-mgr> install git
```
* curl :
```
  sudo <pkg-mgr> install curl
```
3. Configure your git environment( you may skip this option if you have git already configured ) :
```
  git config --global user.email "your_email@example.com"
  git config --global user.name "Your name"
```
4. Generate SSH Key
```
  ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
```
```
  eval "$(ssh-agent -s)"
  ssh-add ~/.ssh/id_rsa
  cat ~/.ssh/id_rsa.pub
```
* To GitGub -> Settings -> SSH and GPG keys
* Click **New SSH Key**
* Paste **id_rsa.pub**, and Add SSH Key
* Test SSH
```
  ssh -T git@github.com
```
5. To build the Linux BSP, please follow the steps :
* First time setup ( you need to have rights to execute sudo apt-get <cmd> without password ) :
```
  sudo apt update
  ./sources/meta-alb/scripts/host-prepare.sh
```
* Creating Build Directories and Testing the Installation. Now you can create a build directory in the SDK root with :
```
  For example, if targeting S32G3 EVB, machine is s32g399ardb3
  source nxp-setup-alb.sh -m s32g399ardb3
```
* When this is done, a bitbake <imagename>, e.g.
```
  bitbake fsl-image-base
```
* would be enough to completely build U-Boot, kernel, modules, the TF-A and a rootfs ready to be deployed. Look
  for a build result in <builddirectory>/tmp/deploy/images/
