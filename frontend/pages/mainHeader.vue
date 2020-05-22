<template>
    <div id="mainheader">
        <h1>메인헤더</h1>
        <button id="alarmBtn">알람</button>
        <alarm />
    </div>
</template>

<script>
import Alarm from '~/components/main/Alarm.vue'

export default {
    components: {
        Alarm,
    },
}
</script>

<style></style>
