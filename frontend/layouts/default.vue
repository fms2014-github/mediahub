<template>
    <div id="app">
        <nav-bar />
        <div id="content">
            <side-bar v-if="labelRefresh" />
            <nuxt id="router-view" :key="$route.fullPath" />
        </div>
    </div>
</template>

<script>
import { mapState } from 'vuex'
import navBar from '@/components/navBar.vue'
import sideBar from '@/components/sideBar.vue'
export default {
    components: {
        navBar,
        sideBar,
    },
    computed: {
        ...mapState(['labelRefresh']),
    },
}
</script>

<style lang="scss" scoped>
#app {
    width: 100%;
    min-width: calc(1280px + 74px);
    background-color: rgb(240, 240, 240);
    overflow: auto;
    #router-view {
        margin: 0 0 0 74px;
    }
}
</style>
