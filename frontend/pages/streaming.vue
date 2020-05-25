<template>
    <div id="streaming">
        <div id="live-component">
            <client-only placeholder="loading...">
                <live-video></live-video>
            </client-only>
        </div>
        <hr />
        <h1>스트리머 A의 다른 영상</h1>
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
}
</script>

<style lang="scss" scoped>
#view-component {
    width: calc(100% - 74px);
    flex-shrink: 0;
    height: 100%;
    min-width: 1280px;
    #live-component {
        width: 100%;
        #live-video {
            height: calc(100vh - 256px);
            min-height: 409.5px;
        }
    }
    hr {
        margin: 1%;
        box-shadow: 0px 0px 2px 1px rgb(180, 180, 180);
        background-color: rgb(180, 180, 180);
        border: {
            width: 0px;
        }
        height: 1px;
        clear: both;
    }
    h1 {
        margin: 20px 0 10px 20px;
        font: {
            weight: 800;
            size: 2rem;
        }
    }
}
</style>
