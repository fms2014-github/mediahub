<template>
    <div id="side-bar">
        <button id="logo" @click="logo">로고</button>
        <div id="tree"></div>
    </div>
</template>

<script>
import '../assets/label.scss'
import { mapGetters } from 'vuex'
import liveBroadcast from '@/assets/icon/liveBroadcast.svg?inline'

export default {
    components: {
        // liveBroadcast,
    },
    data() {
        return {
            labels: null,
        }
    },
    watch: {
        labels(v) {
            var myNode = document.getElementById('tree')
            while (myNode.firstChild) {
                myNode.removeChild(myNode.firstChild)
            }
            console.log('awaaef', v)
            let rootLabelId
            for (const i in v) {
                // console.log('id', v[i].id)
                // console.log('memberId', v[i].memberId)
                // console.log('labelName', v[i].labelName)
                // console.log('superId', v[i].superId)
                if (v[i].superId === -1) {
                    rootLabelId = v[i].id
                }
                const tree = document.getElementById('tree')
                const node = document.createElement('div')
                node.setAttribute('data-label-id', v[i].id)
                node.setAttribute('data-super-id', v[i].superId)
                node.setAttribute('data-member-id', v[i].memberId)
                const button1 = document.createElement('button')
                const button2 = document.createElement('button')
                button1.setAttribute('class', 'add-child-label')
                button1.appendChild(document.createTextNode('+'))
                button1.addEventListener('click', (e) => {
                    const name = prompt('라벨 이름을 적어주세요')
                    console.log(name)
                    if (name !== null) {
                        this.$axios
                            .post(
                                'https://k02d1031.p.ssafy.io:8081/v1/member/label?labelId=' +
                                    e.target.parentNode.parentNode.dataset.labelId +
                                    '&labelName=' +
                                    name,
                                {},
                                { headers: { Authorization: 'Bearer ' + this.getJwt() } },
                            )
                            .then((res) => {
                                console.log(res)
                                this.init()
                            })
                        console.log(button1.parentNode.parentNode)
                    }
                })
                button2.setAttribute('class', 'delete-child-label')
                button2.appendChild(document.createTextNode('-'))
                button2.addEventListener(
                    'click',
                    (e) => {
                        const labelId = e.target.parentNode.parentNode.dataset.labelId
                        console.log(labelId)
                        this.$axios
                            .delete('https://k02d1031.p.ssafy.io:8081/v1/member/label?labelId=' + labelId, {
                                headers: { Authorization: 'Bearer ' + this.getJwt() },
                                params: {},
                            })
                            .then((res) => {
                                console.log(res.status)
                                this.init()
                            })
                    },
                    true,
                )
                if (v[i].superId === -1) {
                    node.setAttribute('id', 'label-wrap')
                    const img = document.createElement('img')
                    const wrap = document.createElement('div')
                    wrap.setAttribute('id', 'root-label-wrap')
                    wrap.appendChild(img)
                    wrap.appendChild(button1)
                    img.setAttribute('id', 'root-label')
                    img.setAttribute('src', '/tree-strcuture.png')
                    img.setAttribute('droppable', 'true')
                    node.appendChild(wrap)
                    tree.appendChild(node)
                    img.addEventListener('drop', (e) => {
                        e.preventDefault()
                        e.stopPropagation()
                        console.log('roottarget', e.target)
                        console.log('root', e.target.parentNode)
                        const channelId = e.dataTransfer.getData('targetId')
                        e.target.parentNode.parentNode.insertBefore(
                            document.querySelector('div[data-channel-id="' + channelId + '"]'),
                            document.querySelector('#label-wrap>.channel'),
                        )
                        const labelId = e.target.parentNode.parentNode.dataset.labelId
                        this.$axios
                            .put(
                                `https://k02d1031.p.ssafy.io:8081/v1/member/channel?channelId=${channelId}&labelId=${labelId}`,
                                {},
                                {
                                    headers: { Authorization: 'Bearer ' + this.getJwt() },
                                },
                            )
                            .then((res) => {
                                console.log(res.status)
                            })
                    })
                    img.addEventListener(
                        'dragover',
                        (e) => {
                            e.preventDefault()
                            e.stopPropagation()
                        },
                        true,
                    )
                } else {
                    const parentLabel = document.querySelector(`div[data-label-id='${v[i].superId}']`)
                    if (rootLabelId === v[i].superId) {
                        const span1 = document.createElement('span')
                        const span2 = document.createElement('span')
                        const dropCap = document.createElement('div')
                        const dropCapWrap = document.createElement('div')
                        const hr = document.createElement('hr')
                        dropCapWrap.setAttribute('class', 'drop-cap-wrap')
                        dropCapWrap.setAttribute('droppable', 'true')
                        dropCap.setAttribute('class', 'drop-cap')
                        span1.setAttribute('class', 'drop-cap-char')
                        span1.appendChild(document.createTextNode(v[i].labelName[0]))
                        dropCap.appendChild(span1)

                        span2.appendChild(document.createTextNode(v[i].labelName))
                        span2.setAttribute('class', 'label-title')
                        span2.appendChild(button1)
                        span2.appendChild(button2)
                        span2.setAttribute('droppable', 'true')
                        node.appendChild(span2)
                        dropCapWrap.appendChild(dropCap)
                        dropCapWrap.appendChild(node)
                        node.setAttribute('class', 'child-label')
                        parentLabel.insertBefore(dropCapWrap, parentLabel.children[1])
                        dropCapWrap.addEventListener('drop', (e) => {
                            e.preventDefault()
                            e.stopPropagation()
                            console.log('drop1target', e.target)
                            console.log('drop1', e.target.children[1])
                            const channelId = e.dataTransfer.getData('targetId')
                            e.target.children[1].appendChild(document.querySelector('div[data-channel-id="' + channelId + '"]'))
                            const labelId = e.target.children[1].dataset.labelId
                            this.$axios
                                .put(
                                    `https://k02d1031.p.ssafy.io:8081/v1/member/channel?channelId=${channelId}&labelId=${labelId}`,
                                    {},
                                    {
                                        headers: { Authorization: 'Bearer ' + this.getJwt() },
                                    },
                                )
                                .then((res) => {
                                    console.log(res.status)
                                })
                        })
                        span2.addEventListener(
                            'drop',
                            (e) => {
                                e.preventDefault()
                                e.stopPropagation()
                                console.log('drop2target', e.target)
                                console.log('drop2', e.target.parentNode)
                                // node.parentNode.removeEventListener('drop', dropEvent1)
                                const channelId = e.dataTransfer.getData('targetId')
                                e.target.parentNode.appendChild(document.querySelector('div[data-channel-id="' + channelId + '"]'))
                                const labelId = e.target.parentNode.dataset.labelId
                                this.$axios
                                    .put(
                                        `https://k02d1031.p.ssafy.io:8081/v1/member/channel?channelId=${channelId}&labelId=${labelId}`,
                                        {},
                                        {
                                            headers: { Authorization: 'Bearer ' + this.getJwt() },
                                        },
                                    )
                                    .then((res) => {
                                        console.log(res.status)
                                    })
                            },
                            true,
                        )
                        span2.addEventListener(
                            'dragover',
                            (e) => {
                                e.preventDefault()
                                e.stopPropagation()
                            },
                            true,
                        )
                        dropCapWrap.addEventListener(
                            'dragover',
                            (e) => {
                                e.preventDefault()
                                e.stopPropagation()
                            },
                            true,
                        )
                    } else {
                        const span = document.createElement('span')
                        const hr = document.createElement('hr')
                        span.appendChild(document.createTextNode(v[i].labelName))
                        span.setAttribute('class', 'label-title')
                        span.appendChild(button1)
                        span.appendChild(button2)
                        span.setAttribute('droppable', 'false')
                        node.appendChild(span)
                        node.setAttribute('class', 'child-label2')
                        parentLabel.insertBefore(node, document.querySelector(`div[data-label-id='${v[i].superId}']` + '>.channel'))
                        node.setAttribute('droppable', 'true')
                        span.addEventListener(
                            'drop',
                            (e) => {
                                e.preventDefault()
                                e.stopPropagation()
                                console.log('drop2target', e.target)
                                console.log('drop2', e.target.parentNode)
                                // node.parentNode.removeEventListener('drop', dropEvent1)
                                const channelId = e.dataTransfer.getData('targetId')
                                e.target.parentNode.appendChild(document.querySelector('div[data-channel-id="' + channelId + '"]'))
                                const labelId = e.target.parentNode.dataset.labelId
                                this.$axios
                                    .put(
                                        `https://k02d1031.p.ssafy.io:8081/v1/member/channel?channelId=${channelId}&labelId=${labelId}`,
                                        {},
                                        {
                                            headers: { Authorization: 'Bearer ' + this.getJwt() },
                                        },
                                    )
                                    .then((res) => {
                                        console.log(res.status)
                                    })
                            },
                            true,
                        )
                        span.addEventListener(
                            'dragover',
                            (e) => {
                                e.preventDefault()
                                e.stopPropagation()
                            },
                            true,
                        )
                    }
                }
                for (const k in v[i].channels) {
                    // console.log(k, v[i].channels[k])

                    const channel = document.createElement('div')
                    const parentLabel = document.querySelector(`div[data-label-id='${v[i].channels[k].labelId}']`)
                    channel.setAttribute('class', 'channel')
                    channel.setAttribute('data-channel-channel-id', v[i].channels[k].channelId)
                    channel.setAttribute('data-channel-description', v[i].channels[k].description)
                    channel.setAttribute('data-channel-display-name', v[i].channels[k].displayName)
                    channel.setAttribute('data-channel-follower', v[i].channels[k].follower)
                    channel.setAttribute('data-channel-label-id', v[i].channels[k].labelId)
                    channel.setAttribute('data-channel-id', v[i].channels[k].id)
                    channel.setAttribute('data-channel-name', v[i].channels[k].name)
                    channel.setAttribute('data-channel-profileImg', v[i].channels[k].profileImg)
                    channel.setAttribute('data-channel-provider', v[i].channels[k].provider)
                    channel.setAttribute('data-channel-subscriber', v[i].channels[k].subscriber)
                    channel.setAttribute('draggable', 'true')
                    const nuxtLink = document.createElement('a')
                    const img = document.createElement('img')
                    nuxtLink.appendChild(img)
                    nuxtLink.setAttribute('href', `/channel/${v[i].channels[k].provider},${v[i].channels[k].channelId}`)
                    img.setAttribute('src', v[i].channels[k].profileImg)
                    img.style.width = '34px'
                    img.style.borderRadius = '17px'
                    channel.appendChild(nuxtLink)
                    // channel.appendChild(
                    //     document.createTextNode(
                    //         `${v[i].channels[k].channelId}, ${v[i].channels[k].provider}`
                    //     )
                    // )
                    parentLabel.appendChild(channel)
                    channel.addEventListener('dragstart', (e) => {
                        console.log('dragstart', channel)
                        e.dataTransfer.setData('targetId', channel.dataset.channelId)
                    })
                }
            }
            const childElement = document.getElementsByClassName('child-label')
            if (childElement !== null) {
                for (let i = 0; i < childElement.length; i++) {
                    childElement[i].style.paddingRight = '8px'
                    childElement[i].style.paddingLeft = '32px'
                    childElement[i].style.paddingTop = '8px'
                    childElement[i].style.paddingBottom = '8px'
                }
            }
            const childElement2 = document.getElementsByClassName('child-label2')
            if (childElement2.length !== 0) {
                for (let i = 0; i < childElement2.length; i++) {
                    childElement2[i].style.paddingRight = '8px'
                    childElement2[i].style.paddingLeft = '24px'
                }
            }
            const dropCapElement = document.getElementsByClassName('drop-cap')
            for (let i = 0; i < dropCapElement.length; i++) {
                dropCapElement[i].addEventListener('mouseover', () => {
                    dropCapElement[i].classList.add('add-shadow')
                    dropCapElement[i].parentNode.children[1].classList.add('expand-list')
                })
                dropCapElement[i].addEventListener('mouseleave', () => {
                    dropCapElement[i].classList.remove('add-shadow')
                    dropCapElement[i].parentNode.children[1].classList.remove('expand-list')
                })
                dropCapElement[i].parentNode.addEventListener('mouseover', () => {
                    dropCapElement[i].parentNode.children[1].classList.add('expand-list')
                })
                dropCapElement[i].parentNode.addEventListener('mouseleave', () => {
                    dropCapElement[i].parentNode.children[1].classList.remove('expand-list')
                })
            }
            const labelTitleElement = document.getElementsByClassName('label-title')
            for (let i = 0; i < labelTitleElement.length; i++) {
                labelTitleElement[i].addEventListener('mouseover', () => {})
            }
        },
    },
    mounted() {
        this.init()
    },
    methods: {
        ...mapGetters({ getJwt: 'login/getJwt' }),
        async init() {
            console.log('jwt', this.getJwt())
            const Info = (
                await this.$axios.get('https://k02d1031.p.ssafy.io:8081/v1/member/information', {
                    headers: { Authorization: 'Bearer ' + this.getJwt() },
                })
            ).data

            localStorage.setItem('auth', JSON.stringify(Info.auth))
            localStorage.setItem('labels', JSON.stringify(Info.label))
            this.labels = Info.label
        },
        logo() {
            this.$router.push('/')
        },
    },
}
</script>

<style lang="scss" scoped>
@import '../assets/commonMixin';
#side-bar {
    display: flex;
    position: fixed;
    top: 0px;
    height: 100%;
    flex-direction: column;
    min-width: $side-bar-width;
    background-color: white;
    align-self: stretch;
    z-index: 9999;
    svg {
        width: 42px;
    }
    #logo,
    #logo:focus {
        width: 100%;
        height: 65px;
        border: {
            width: 0px;
        }
        background-color: white;
        outline: none;
        cursor: pointer;
    }
}
</style>
