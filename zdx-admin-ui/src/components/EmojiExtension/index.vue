<template>
	<DropdownToolbar title="emoji" :visible="state.visible" @onChange="onChange">
		<template #overlay>
			<div class="emoji-container">
				<ol class="emojis">
					<li
						v-for="(emoji, index) of emojis"
						:key="`emoji-${index}`"
						@click="emojiHandler(emoji)"
						v-text="emoji"
					></li>
				</ol>
			</div>
		</template>
		<template #trigger>
			<svg class="md-editor-icon" aria-hidden="true">
				<use xlink:href="#icon-emoji"></use>
			</svg>
		</template>
	</DropdownToolbar>
</template>

<script setup>
import { DropdownToolbar } from 'md-editor-v3';
import { reactive } from 'vue';
import { emojis } from './data';
const props = defineProps({
	onInsert: {
		type: Function,
		default: () => () => null
	}
});
const state = reactive({
	visible: false
});
const emojiHandler = (emoji) => {
	const generator = () => {
		return {
			targetValue: emoji,
			select: true,
			deviationStart: 0,
			deviationEnd: 0
		};
	};
	props.onInsert(generator);
};
const onChange = (visible) => {
	state.visible = visible;
};
</script>

<style lang="scss" scoped>
.emoji-container {
	border-radius: 3px;
	border: 1px solid #e6e6e6;
}

.emojis {
	position: relative;
	width: 363px;
	margin: 10px;
	padding: 0;
	background-color: #fff;

	li {
		cursor: pointer;
		float: left;
		border: 1px solid #e8e8e8;
		font-size: 18px;
		height: 34px;
		width: 34px;
		overflow: hidden;
		margin: -1px 0 0 -1px;
		padding: 4px 2px;
		text-align: center;
		list-style: none;
		z-index: 11;

		&:hover {
			position: relative;
			border: 1px solid #63a35c;
			z-index: 12;
		}
	}

	&::after {
		content: '';
		clear: left;
		display: block;
	}

	* {
		user-select: none;
	}
}
</style>