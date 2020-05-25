<template>
    <div id="uploaded-page">
        <client-only placeholder="loading...">
            <upload-video />
        </client-only>
        <sub-button />
    </div>
</template>

<script>
import uploadVideo from '~/components/uploadVideo.vue'
import subButton from '~/components/button.vue'
export default {
    components: {
        uploadVideo,
        subButton,
    },
}
</script>

<style lang="scss" scoped>
#uploaded-page {
    display: block;
    width: calc(100% - 72px);
    height: 100%;
}
</style>
