<template>
  <div class="content">
    <div class="table">
      <table>
        <thead>
          <th>Finder</th>
          <th>Fundort</th>
          <th>Beschreibung</th>
        </thead>
        <tbody>
          <Zeile :finder="sichtung.finder" :place="sichtung.place" :description="sichtung.description" v-for="sichtung in sichtungslistenitems" :key="sichtung.id"/>
        </tbody>
      </table>
    </div>
    <div>
      <button @click="refreshList()">Liste erneuern</button>
    </div>
  </div>
</template>
<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import ISichtung from 'src/components/ISichtung';
import Zeile from './Zeile.vue';
export default class sichtungsliste extends Vue {
    private ListItems : ISichtung [] = [];


    private refreshList() : void {
        let tmplist: ISichtung[] = [];
        fetch('/rest/sichtungen')
        .then((response) => {
            if (!response.ok) {
                throw new Error('doof gelaufen');
            }
            return response.json();
        }).then((responsedata) => {
            for (const sichtung of responsedata) {
                fetch(sichtung).then((response) => {
                    if (!response.ok) {
                        throw new Error(`fehler bei sichtung ${sichtung}`);
                    }
                    return response.json();
                }).then((jsondata: ISichtung) => {
                    tmplist.push(jsondata);
                })
                .catch( (error) => {
                });
            }
        });
        this.ListItems = tmplist;
    }
    private get sichtungslistenitems(): ISichtung[] {
     return this.ListItems;
    }
}
</script>
<style scoped>
.content{
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
}
.table{
    width: 100%;
}
table{
    align: center;
}
</style>

