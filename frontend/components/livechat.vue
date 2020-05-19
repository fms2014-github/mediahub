<template>
    <div id="livechat-component">
        <div id="streamer-name">
            스트리머 A채팅방 입니다.
        </div>
        <div id="message-list">
            <div v-for="item in testlist" :key="item.id" class="receive-message-list">
                <div v-if="!item.me" class="receive-message">
                    <img class="profile-image" src="https://via.placeholder.com/24" />
                    <span>채팅 기록{{ item.message }} 입니다.</span>
                </div>
                <div v-if="item.me" class="receive-message my-message">
                    <span>채팅 기록{{ item.message }} 입니다.</span>
                    <img class="profile-image" src="https://via.placeholder.com/24" />
                </div>
            </div>
        </div>
        <div id="send-message">
            <div id="select-platform">
                <span id="youtube" class="enable-youtube">YT</span>
                <span id="twitch" class="enable-twitch">TW</span>
            </div>
            <input type="text" name="message" />
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            testlist: [
                { me: false, message: 1 },
                { me: false, message: 2 },
                { me: false, message: 3 },
                { me: false, message: 4 },
                { me: false, message: 5 },
                { me: false, message: 6 },
                { me: true, message: 7 },
                { me: false, message: 8 },
                { me: false, message: 9 },
                { me: false, message: 10 },
                { me: false, message: 11 },
                { me: false, message: 12 },
                { me: false, message: 13 },
                { me: true, message: 14 },
                { me: false, message: 15 },
                { me: false, message: 16 },
                { me: false, message: 17 },
                { me: false, message: 18 },
            ],
        }
    },
}
</script>

<style lang="scss" scoped>
@import '~/assets/commonMixin';
#livechat-component {
    width: 260px;
    height: 500px;
    #streamer-name {
        height: 36px;
        padding: 5px;
        background: linear-gradient(
            to bottom,
            rgb(205‬, 205‬, 205‬),
            rgb(185, 185, 185),
            rgb(170, 170, 170),
            rgb(185, 185, 185),
            rgb(205‬, 205‬, 205‬)
        );
    }
    #message-list {
        padding: 0 8px;
        height: calc(100% - 36px - 48px);
        overflow: auto;
        @include scrollbar('&');
        border-bottom-width: 1px;
        border-bottom-color: #cdcdcd;
        border-bottom-style: solid;
        img {
            vertical-align: middle;
            border-radius: 100%;
        }
        span {
            vertical-align: middle;
        }
        .receive-message {
            margin: 12px 0px;
        }
        .my-message {
            text-align: right;
        }
    }
    #send-message {
        height: 48px;
        margin-top: 4px;
        #select-platform {
            display: inline-block;
            width: 10%;
            height: 100%;
            margin: 2px;
            span {
                display: inline-block;
                width: 100%;
                vertical-align: top;
                text-align: center;
                font: {
                    size: 0.8rem;
                }
                border-radius: 3px;
                background-color: rgb(150, 150, 150);
            }

            .enable-youtube {
                background-color: $youtube-color;
                color: white;
            }
            .enable-twitch {
                background-color: $twitch-color;
                color: white;
            }
        }
        input[name='message'] {
            vertical-align: top;
            width: calc(100% - 10% - 10px);
            margin: 2px 0;
            padding: 10px 5px;
        }
    }
}
</style>
