DESCRIPTION = "thead CSI Camera HAL"
HOMEPAGE = "https://code.aone.alibaba-inc.com/light_sdk/csi_hal"
LICENSE = "CLOSED"
#LIC_FILES_CHKSUM = ""

COMPATIBLE_MACHINE = "light-*"

DEPENDS += " openssl cmake python3 zlib linux-thead process-linker image-proprietary video-memory"
DEPENDS += "virtual/kernel linux-libc-headers"
SRC_URI = " \
            git://gogs@trac.godzil.net/thead-yocto/csi_hal.git;branch=master;protocol=http \
          "

THEAD_BSP_TAG ?= "${AUTOREV}"
SRCREV = "${THEAD_BSP_TAG}"

S = "${WORKDIR}/git"

export SYSROOT_DIR="${RECIPE_SYSROOT}"
#export SYSROOT_DIR="${RECIPE_SYSROOT_NATIVE}"

export PROJECT_DIR?="${COREBASE}/.."
export ARCH?="riscv"
export BOARD_NAME="${MACHINEOVERRIDES}"
export BUILD_ROOT?="${TOPDIR}"
export BUILDROOT_DIR?="${BUILD_ROOT}"
export CROSS_COMPILE="riscv64-linux-"
export TOOLCHAIN_DIR?="${EXTERNAL_TOOLCHAIN}"
export LINUX_DIR?="${STAGING_KERNEL_BUILDDIR}"
export INSTALL_DIR_ROOTFS?="${IMAGE_ROOTFS}"
export INSTALL_DIR_SDK?="${SDK_DEPLOY}" 
export PATH="${SYSROOT_DIR}:${SYSROOT_DIR}/usr/include:${SYSROOT_DIR}/usr/lib:${SYSROOT_DIR}/lib:${SYSROOT_DIR}/include:${RECIPE_SYSROOT_NATIVE}/usr/bin/riscv64-oe-linux:${COREBASE}/scripts:${COREBASE}/bitbake/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games:/snap/bin"
export KERNEL_VERSION="$(cat ${BASE_WORKDIR}/kernel_version)"

#EXTRA_OEMAKE+="BUILD_SYSTEM='YOCTO_BUILD' PLATFORM=light -C src/platform/light 'CFLAGS=${CFLAGS}'"
export EXTRA_OEMAKE+="BUILD_SYSTEM='YOCTO_BUILD' PLATFORM=light "
export EXTRA_OEMAKE+="VISYS_SYM_PATH=${PKG_CONFIG_SYSROOT_DIR}"

PARALLEL_MAKEINST = "-j8"

do_compile() {
    oe_runmake
}

do_install() {
    install -d ${D}${libdir}/csi_hal
    install -d ${D}${datadir}/csi_hal
    install -d ${D}${includedir}/csi_hal
    install -m 0644 ${S}/output/hal/lib_so/*.so                          ${D}${libdir}/csi_hal
    install -m 0644 ${S}/include/lib_camera/*.h                          ${D}${includedir}/csi_hal
    install -m 0644 ${S}/include/common/*.h                              ${D}${includedir}/csi_hal
    install -m 0644 ${S}/output/hal/lib_so/libhal_camera.so              ${D}${libdir}
    install -m 0755 ${S}/output/examples/camera/*                        ${D}${datadir}/csi_hal
}

FILES:${PN} += " ${base_libdir} "
FILES:${PN} += " ${libdir} "
FILES:${PN} += " ${datadir} "
FILES:${PN} += " ${includedir} "
INSANE_SKIP:${PN} += " debug-files staticdev file-rdeps "

PACKAGES = "${PN}"

# RDEPENDS:${PN} = " "
