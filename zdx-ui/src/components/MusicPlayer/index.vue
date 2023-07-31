<template>
	<div id="player"></div>
</template>

<script setup>
import 'aplayer/dist/APlayer.min.css';
import APlayer from 'aplayer';
import {onMounted, ref} from "vue";
import {music} from "@/api/blog";
import useStore from "@/stores";
const { blog } = useStore();

onMounted(() => {
	init()
})
const ap = ref()
const musics = ref([])
const init = () => {
	ap.value = new APlayer({
		container: document.getElementById('player'),
		autoplay: false,
		theme: '#e63c5b',
		loop: 'all',
		fixed: true,
		float: true,
		order: 'random',
		preload: 'auto',
		volume: 0.7,
		mutex: true,
		listFolded: true,
		listMaxHeight: 90,
	});
	music().then((res) => {
		musics.value = res.data
		ap.value.list.add(musics.value)
	})
}
</script>

<style scoped></style>
