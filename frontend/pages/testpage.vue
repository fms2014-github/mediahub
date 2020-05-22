<template>
<<<<<<< HEAD
    <div>
        <nav-bar></nav-bar>
=======
    <div id="testpage">
        <clint-only placeholder="loding...">
            <live-video></live-video>
        </clint-only>
        <livechat></livechat>
>>>>>>> f68583ffe534cc0b1429d1747be134985edcc2b5
    </div>
</template>

<script>
<<<<<<< HEAD
import navBar from '@/components/navBar.vue'
export default {
    components: {
        navBar,
=======
import livechat from '@/components/livechat.vue'
import liveVideo from '@/components/liveVIdeo.vue'

export default {
    components: {
        livechat,
        liveVideo,
>>>>>>> f68583ffe534cc0b1429d1747be134985edcc2b5
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
