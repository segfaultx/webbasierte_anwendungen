<template>
    <tr>
        <td>
            {{finder}}
        </td>
        <td>
            {{place}}
        </td>
        <td>
            {{description}}
        </td>
    </tr>
</template>
<script lang="ts">

import { Vue, Component, Prop } from 'vue-property-decorator';

@Component
export default class Zeile extends Vue {
    @Prop() private finder!: string;
    @Prop() private place!: string;
    @Prop() private description!: string;
}
</script>
<style scoped>

</style>
