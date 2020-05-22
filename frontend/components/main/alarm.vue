<template>
    <div id="alarm">
        <div class="alarmDiv">
            <div v-for="a in alarmList" :key="a" class="alarmList">
                <div class="profile"><img class="profileImg" src="~/assets/images/mainHeader/profile2.png" alt="" /></div>
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

<style>
.alarmDiv {
    width: 300px;
    max-height: 500px;
    overflow-y: scroll;
    padding: 15px;
    padding-top: 10px;
    background: rgba(250, 243, 204, 0.815);
}
.alarmDiv::-webkit-scrollbar {
    width: 8px;
}
.alarmDiv::-webkit-scrollbar-track {
    background-color: rgba(241, 238, 215, 0.815);
    /* box-shadow: inset 0px 0px 5px black; */
}
.alarmDiv::-webkit-scrollbar-thumb {
    background-color: rgba(226, 182, 182, 0.815);
    box-shadow: inset 0px 0px 1px rgb(94, 94, 94);

    border-radius: 10px;
}
.alarmList {
    margin: 20px 10px;
    display: flex;
    /* background: rgba(255, 192, 203, 0.5); */
}
.profileImg {
    width: 40px;
    height: 40px;
}
.contents {
    margin: 5px 0 0 10px;
}
.content {
    font-size: 12px;
}
.date {
    font-size: 12px;
    color: rgb(156, 156, 156);
}
</style>
