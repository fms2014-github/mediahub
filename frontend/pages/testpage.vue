<template>
    <div id="testpage">
        <clint-only placeholder="loding...">
            <live-video></live-video>
        </clint-only>
        <livechat></livechat>
    </div>
</template>

<script>
import livechat from '@/components/livechat.vue'
import liveVideo from '@/components/liveVIdeo.vue'

export default {
    components: {
        livechat,
        liveVideo,
    },
    mounted() {},
}
</script>

<style lang="scss" scoped>
#testpage {
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    #livechat-component {
        flex-shrink: 0;
    }
}
</style>
