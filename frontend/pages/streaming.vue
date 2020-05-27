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
#router-view {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
    height: calc(100% - 58px);
    #live-component {
        width: 156.25vh;
        padding-right: 340px;
    }
    hr {
        width: 100%;
        margin: 1%;
        box-shadow: 0px 0px 2px 1px rgb(180, 180, 180);
        background-color: rgb(180, 180, 180);
        border: {
            width: 0px;
        }
        height: 1px;
        clear: both;
        flex-grow: 100;
    }
    h1 {
        margin: 20px 0 10px 20px;
        font: {
            weight: 800;
            size: 2rem;
        }
        flex-wrap: wrap;
    }
}
</style>
