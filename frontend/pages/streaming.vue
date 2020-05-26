<template>
    <div id="streaming">
        <div id="live-component">
            <client-only placeholder="loading...">
                <live-video></live-video>
            </client-only>
        </div>
        <!--<hr />
        <h1>스트리머 A의 다른 영상</h1>
        <video-form />-->
    </div>
</template>

<script>
import liveVideo from '@/components/liveVideo.vue'

import videoForm from '@/components/main/videoForm.vue'

export default {
    components: {
        liveVideo,
        // videoForm,
    },
}
</script>

<style lang="scss" scoped>
#streaming {
    margin: 58px 0 0 74px;
    display: flex;
    justify-content: center;
    align-items: center;
    height: calc(100vh - 58px);
    #live-component {
        width: 156.25vh;
        padding-right: 340px;
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
