<template>
    <div id="main-page">
        <client-only placeholder="loading...">
            <live-video-slide-bar />
        </client-only>
        <video-form />
    </div>
</template>

<script>
import liveVideoSlideBar from '~/components/liveVideoSlideBar.vue'
import videoForm from '~/components/main/videoForm.vue'
export default {
    components: {
        liveVideoSlideBar,
        videoForm,
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
