<template>
    <div id="uploaded-page">
        <div id="upload-container">
            <client-only placeholder="loading...">
                <upload-video :play-info="playInfo" />
            </client-only>
            <sub-button :play-info="playInfo" />
            <hr />
            <h1>추천 영상</h1>
            <video-form />
        </div>
    </div>
</template>

<script>
import uploadVideo from '~/components/uploadVideo.vue'
import subButton from '~/components/button.vue'
import videoForm from '~/components/main/videoForm.vue'
export default {
    components: {
        uploadVideo,
        subButton,
        videoForm,
    },
    data: () => {
        return {
            playInfo: {
                kind: 'y',
                play: 'fvjpE_wFL5A',
                channelName: 'Lauv',
                // play: '624698735',
                // kind: 't',
            },
        }
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
