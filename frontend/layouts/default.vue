<template>
    <div id="app">
        <nav-bar />
        <div id="content">
            <side-bar />
            <nuxt id="router-view" />
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
    width: 100%;
    min-width: calc(1280px + 74px);
    background-color: rgb(240, 240, 240);
    #router-view {
        margin: 58px 0 0 74px;
    }
}
</style>
