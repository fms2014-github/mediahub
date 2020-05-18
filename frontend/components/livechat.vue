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
                <span id="youtube">YT</span>
                <span id="twitch">TW</span>
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
#livechat-component {
    width: 260px;
    #streamer-name {
        padding: 10px;
        background: linear-gradient(to bottom, rgb(220, 220, 220), rgb(195, 195, 195), rgb(180, 180, 180), rgb(195, 195, 195), rgb(220, 220, 220));
    }
    #message-list {
        padding: 0 8px;
        height: 500px;
        overflow: auto;
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
        #select-platform {
            display: inline-block;
            width: 10%;
            height: 100%;
        }
        input[name='message'] {
            vertical-align: top;
            width: 87%;
            padding: 10px 5px;
        }
    }
}
</style>
