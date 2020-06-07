<template>
    <div id="uploaded-page">
        <div id="upload-container">
            <client-only placeholder="loading...">
                <upload-video v-if="playInfo.play !== ''" :play-info="playInfo" />
            </client-only>
            <sub-button v-if="playInfo.channelId !== ''" :play-info="playInfo" />
            <hr />
            <h1>추천 영상</h1>
            <video-form />
        </div>
    </div>
</template>

<script>
import axios from 'axios'
import uploadVideo from '~/components/uploadVideo.vue'
import subButton from '~/components/button.vue'
import videoForm from '~/components/main/videoForm.vue'
export default {
    middleware: 'authenticated',
    components: {
        uploadVideo,
        subButton,
        videoForm,
    },
    asyncData({ params }) {
        const videoId = params.videoId
        return { videoId }
    },
    data: () => {
        return {
            playInfo: {
                kind: 'twitch',
                play: '',
                channelId: '',
            },
        }
    },
    created() {},
    mounted() {
        this.playInfo.play = this.videoId
        const fragmentString = window.location.search.replace('?', '')
        // Parse query string to see if page request is coming from OAuth 2.0 server.
        const params = {}
        const regex = /([^&=]+)=([^&]*)/g
        const m = regex.exec(fragmentString)
        this.playInfo.channelId = decodeURIComponent(m[2])
    },
}
</script>

<style lang="scss" scoped>
#router-view {
    display: flex;
    justify-content: center;
    width: calc(100% - 72px);
    height: 100%;

    #upload-container {
        width: 70%;
    }
}
</style>
