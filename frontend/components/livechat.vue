<template>
    <div id="livechat-component">
        <div id="streamer-name">
            스트리머 A채팅방 입니다.
        </div>
        <div id="message-list">
            <div v-for="item in testlist" :key="item.id" class="receive-message-list">
                <div v-if="!item.me" class="receive-message">
                    <div class="profile">
                        <img class="profile-image" src="https://via.placeholder.com/24" />
                        <span>{{ item.nickname }}</span>
                    </div>
                    <span>채팅 기록{{ item.message }} 입니다.</span>
                </div>
                <div v-if="item.me" class="receive-message my-message">
                    <div class="profile">
                        <img class="profile-image" src="https://via.placeholder.com/24" />
                        <span>{{ item.nickname }}</span>
                    </div>
                    <span>채팅 기록{{ item.message }} 입니다.</span>
                </div>
            </div>
        </div>
        <div id="send-message">
            <div id="select-platform">
                <span class="youtube" @click="selectPlatform">YT</span>
                <span class="twitch platform-disable" @click="selectPlatform">TW</span>
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
                { nickname: '상대방', me: false, message: 1 },
                { nickname: '상대방', me: false, message: 2 },
                { nickname: '상대방', me: false, message: 3 },
                { nickname: '상대방', me: false, message: 4 },
                { nickname: '상대방', me: false, message: 5 },
                { nickname: '상대방', me: false, message: 6 },
                { nickname: '나', me: true, message: 7 },
                { nickname: '상대방', me: false, message: 8 },
                { nickname: '상대방', me: false, message: 9 },
                { nickname: '상대방', me: false, message: 10 },
                { nickname: '상대방', me: false, message: 11 },
                { nickname: '상대방', me: false, message: 12 },
                { nickname: '상대방', me: false, message: 13 },
                { nickname: '나', me: true, message: 14 },
                { nickname: '상대방', me: false, message: 15 },
                { nickname: '상대방', me: false, message: 16 },
                { nickname: '상대방', me: false, message: 17 },
                { nickname: '상대방', me: false, message: 18 },
            ],
        }
    },
    methods: {
        selectPlatform() {
            const selector = document.querySelectorAll('#select-platform span')
            for (let i = 0; i < selector.length; i++) {
                selector[i].classList.toggle('platform-disable')
            }
        },
    },
}
</script>

<style lang="scss" scoped>
@import '~/assets/commonMixin';
#livechat-component {
    width: 300px;
    height: $video-height;
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
            width: 75%;
            margin-right: calc(25% - 16px);
            padding: 8px;
            background-color: gray;
            border-radius: 8px;
        }
        .my-message {
            margin-left: calc(25% - 16px);
            background-color: rgb(255, 205, 0);
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
                border-radius: 3px;
                font: {
                    size: 0.8rem;
                }
            }
            .youtube {
                background-color: $youtube-color;
                color: white;
            }
            .twitch {
                background-color: $twitch-color;
                color: white;
            }
            .platform-disable {
                background-color: rgb(150, 150, 150);
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
