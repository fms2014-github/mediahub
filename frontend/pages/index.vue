<template>
    <div id="main-page">
        <client-only placeholder="loading...">
            <slide-bar />
        </client-only>
        <video-form />
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
#main-page {
    display: block;
    width: calc(100% - 72px);
    height: 100%;
}
</style>
