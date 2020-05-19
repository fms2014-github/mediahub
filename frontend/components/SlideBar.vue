<template>
    <div id="slide-bar">
        <div id="slide-container">
            <div class="left move-button">test</div>
            <div id="inline-block">
                <div v-for="(item, index) in test" :key="item" :class="'video v' + index">
                    <img src="https://via.placeholder.com/530X300" />
                </div>
            </div>
            <div class="right move-button">test</div>
        </div>
    </div>
</template>

<script>
export default {
    data: () => {
        return {
            test: [1, 1, 1, 1, 11],
        }
    },
}
</script>

<style lang="scss" scoped>
#slide-bar {
    padding-left: 10%;

    #slide-container {
        background-color: aqua;
        position: relative;
        width: 100%;
        height: 400px;

        #inline-block {
            position: absolute;
            display: inline-block;

            background-color: red;
            vertical-align: middle;
            width: 80%;
            height: 400px;
            padding: 20px;
            left: 50%;
            transform: translateX(-50%);

            .video {
                display: inline-block;
                position: absolute;
                top: 50%;
                transform: translateY(-50%);
                border: 1px solid black;
                overflow: hidden;
            }
            .v0 {
                left: 5%;
                width: 30%;
                height: 210px;
            }
            .v1 {
                left: 10%;
                z-index: 1;
                width: 50%;
                height: 255px;
                /* overflow: hidden; */
            }
            .v2 {
                left: 15%;
                width: 70%;
                z-index: 2;
            }
            .v3 {
                right: 10%;
                z-index: 1;
                width: 50%;
                height: 255px;
            }
            .v4 {
                right: 5%;
                z-index: 0;
                width: 30%;
                height: 210px;
            }
        }
        .move-button {
            position: absolute;
            display: inline-block;
            /* width: 5%; */
            height: auto;
            top: 50%;
        }
        .left {
            left: 5%;
            z-index: 10;
        }
        .right {
            right: 5%;
            z-index: 10;
        }
    }
}
</style>
