<template>
    <div id="app">
        <nav-bar />
        <div id="content">
            <side-bar />
            <nuxt />
        </div>
    </div>
</template>

<script>
import navBar from '@/components/navBar.vue'
import sideBar from '@/components/sideBar.vue'
export default {
    components: {
        navBar,
        sideBar,
    },
}
</script>

<style lang="scss" scoped>
#app {
    min-width: 1080px;
    #content {
        display: flex;
    }
}
</style>
