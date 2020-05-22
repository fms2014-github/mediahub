<template>
    <div><nav-bar></nav-bar><side-bar></side-bar><nuxt /></div>
</template>

<script>
import navBar from '@/components/navBar.vue'
import sideBar from '@/components/sidebar.vue'
export default {
    components: {
        navBar,
        sideBar,
    },
}
</script>

<style lang="scss" scoped></style>
