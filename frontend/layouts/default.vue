<template>
    <div id="app">
        <nav-bar />
        <div id="content">
            <side-bar />
            <nuxt id="view-component" />
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
    background-color: rgb(240, 240, 240);
    #view-component {
        margin: 58px 0 0 74px;
    }
}
</style>
