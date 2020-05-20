<template>
    <div id="nav-bar">
        <button><img src="~assets/icon/sign-in.svg" /></button>
        <button>알람</button>
        <button>도움말</button>
    </div>
</template>

<script>
export default {
    mounted() {},
}
</script>

<style lang="scss" scoped>
#nav-bar {
    width: 100vw;
    height: 60px;
    border: {
        style: solid;
        width: 1px;
        color: black;
    }
    button {
        display: inline-block;
        img {
            width: auto;
            height: auto;
        }
    }
}
</style>
