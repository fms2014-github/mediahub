<template>
    <div id="button">
        <div id="button-container">
            <div v-if="kind === 'y'">
                <div class="flex-container">
                    <div class="youtube-white button">가입</div>
                    <div class="youtube-red button">구독</div>
                </div>
            </div>
            <div v-else>
                <div class="flex-container">
                    <div class="twitch button">팔로우</div>
                    <div class="twitch button">구독</div>
                    <div class="twitch button">후원</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: { kind: { type: String, default: '' } },
    created() {},
    mounted() {},
    methods: {},
}
</script>

<style lang="scss" scoped>
#button {
    #button-container {
        // background-color: rgb(40, 40, 40);
        display: inline-block;
        width: 100%;
        height: 60px;

        .flex-container {
            display: inline-flex;
            width: 100%;
            justify-content: flex-end;
        }
        .button {
            margin-left: 2%;
            width: 8%;
            height: 35px;
            text-align: center;
            line-height: 35px;
            font-weight: bold;
            cursor: pointer;
            font-size: 1rem;
        }
        .youtube-white {
            background-color: white;
            border: 1px solid black;
        }
        .youtube-red {
            background-color: #e24821;
            border: 1px solid red;
            color: white;
        }
        .twitch {
            background-color: #d6c7ff;
            border: 1px solid #d6c7ff;
        }
    }
}
</style>
