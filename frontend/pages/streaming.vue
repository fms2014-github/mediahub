<template>
    <div id="streaming">
        <client-only placeholder="loading...">
            <live-video></live-video>
        </client-only>
        <live-chat></live-chat>
        <video-form></video-form>
    </div>
</template>

<script>
import liveChat from '@/components/livechat.vue'
import liveVideo from '@/components/liveVideo.vue'
import videoForm from '@/components/main/videoForm.vue'

export default {
    components: {
        liveChat,
        liveVideo,
        videoForm,
    },
    mounted() {},
}
</script>

<style lang="scss" scoped>
#streaming {
    display: inline-block;
    width: calc(100% - 74px);
    height: calc(100vh - 58px);
    #live-video {
        height: 50vh;
        vertical-align: top;
    }
    #livechat-component {
        height: 50vh;
    }
}
</style>
