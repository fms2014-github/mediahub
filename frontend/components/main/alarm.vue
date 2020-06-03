<template>
    <div id="alarm">
        <div class="alarm-div">
            <div v-for="a in alarmList" :key="a" class="alarm-list">
                <div class="profile"><img class="profile-img" src="~/assets/images/mainHeader/profile2.png" alt="" /></div>
                <!-- <div class="profile"><img :src="a.profile" alt=""></div> -->
                <div class="contents">
                    <div class="content">{{ a.content }}</div>
                    <div class="date">{{ a.date }}</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            alarmList: [
                { date: '09:00', content: '히샨님이 라이브를 시작하였습니다.' },
                { date: '1일전', content: '히샨님이 라이브를 시작하였습니다.' },
                { date: '2일전', content: '히샨님이 라이브를 시작하였습니다.' },
                { date: '3일전', content: '히샨님이 라이브를 시작하였습니다.' },
                { date: '4일전', content: '히샨님이 라이브를 시작하였습니다.' },
                { date: '5일전', content: '히샨님이 라이브를 시작하였습니다.' },
                { date: '6일전', content: '히샨님이 라이브를 시작하였습니다.' },
                { date: '7일전', content: '히샨님이 라이브를 시작하였습니다.' },
                { date: '2020.05.14', content: '히샨님을 구독했습니다.' },
            ],
        }
    },
}
</script>

<style lang="scss" scoped>
.alarm-div {
    width: 280px;
    max-height: 500px;
    overflow-y: scroll;
    box-shadow: 0 0 3px rgb(100, 100, 100);
    border-radius: 5px;
    padding: 15px;
    padding-top: 10px;
    background: rgb(255, 255, 255);
}
.alarm-div::-webkit-scrollbar {
    width: 8px;
}
.alarm-div::-webkit-scrollbar-track {
    // background-color: rgba(241, 238, 215, 0.815);
}
.alarm-div::-webkit-scrollbar-thumb {
    background-color: rgba(226, 182, 182, 0.815);
    box-shadow: inset 0px 0px 1px rgb(94, 94, 94);

    border-radius: 5px;
}
.alarm-list {
    margin: 20px 10px;
    display: flex;
    /* background: rgba(255, 192, 203, 0.5); */
}
.profile-img {
    width: 40px;
    height: 40px;
}
.contents {
    margin: 5px 0 0 10px;
}
.content {
    font-size: 12px;
    margin-bottom: 5px;
}
.date {
    font-size: 11px;
    color: rgb(156, 156, 156);
}
</style>
