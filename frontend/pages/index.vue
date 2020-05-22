<template>
    <div id="mainPage">
        <client-only placeholder="loading...">
            <slide-bar />
        </client-only>
        <video-form></video-form>
    </div>
</template>

<script>
import slideBar from '~/components/slideBar.vue'
import videoForm from '~/components/main/videoForm.vue'
export default {
    components: {
        slideBar,
        videoForm,
    },
}
</script>

<style lang="scss" scoped>
#mainPage {
    display: inline-block;
    width: calc(100% - 74px);
    height: calc(100vh - 62px);
    background-color: rgb(200, 200, 200);
}
</style>
