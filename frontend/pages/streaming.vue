<template>
    <div id="streaming">
        <div id="live-component">
            <client-only placeholder="loading...">
                <live-video></live-video>
            </client-only>
        </div>
        <h1>다른 영상들</h1>
        <video-form />
    </div>
</template>

<script>
import liveVideo from '@/components/liveVideo.vue'
import videoForm from '@/components/main/videoForm.vue'

export default {
    components: {
        liveVideo,
        videoForm,
    },
    mounted() {},
}
</script>

<style lang="scss" scoped>
#streaming {
    display: block;
    width: calc(100% - 72px);
    min-width: 1076px;
    height: 100%;
    #live-component {
        background-color: rgb(220, 220, 220);
    }
}
</style>
