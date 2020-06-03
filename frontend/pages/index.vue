<template>
    <div id="main-page">
        <client-only placeholder="loading...">
            <live-video-slide-bar />
        </client-only>

        <video-form />
    </div>
</template>

<script>
import { mapGetters } from 'vuex'
import liveVideoSlideBar from '~/components/liveVideoSlideBar.vue'
import videoForm from '~/components/main/videoForm.vue'
export default {
    components: {
        liveVideoSlideBar,
        // videoForm,
    },
    mounted() {
        console.log('index', this.jwt())
    },
    methods: {
        ...mapGetters({ jwt: 'login/getJwt' }),
    },
}
</script>

<style lang="scss" scoped>
#router-view {
    display: block;
    width: calc(100% - 72px);
    height: 100%;
}
</style>
