<template>
    <div id="streaming">
        <client-only placeholder="loading...">
            <live-video></live-video>
        </client-only>
        <live-chat></live-chat>
    </div>
</template>

<script>
import liveChat from '@/components/liveChat.vue'
import liveVideo from '@/components/liveVideo.vue'

export default {
    components: {
        liveChat,
        liveVideo,
    },
    mounted() {},
}
</script>

<style lang="scss" scoped>
#streaming {
    display: inline-flex;
    flex-direction: row;
    flex-wrap: nowrap;
    width: calc(100% - 74px);
    height: calc(100vh - 62px);
    #livechat-component {
        flex-shrink: 0;
    }
}
</style>
