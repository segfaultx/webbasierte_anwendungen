<template>
  <div class="content">
      <table>
        <thead>
          <th>Finder</th>
          <th>Fundort</th>
          <th>Beschreibung</th>
        </thead>
        <tbody>
          <Zeile
            :finder="sichtung.finder"
            :place="sichtung.place"
            :description="sichtung.description"
            v-for="sichtung in sichtungslistenitems"
            :key="sichtung.id"
          />
        </tbody>
      </table>
    <div>
      <button @click="refreshList()">Liste erneuern</button>
    </div>
  </div>
</template>
<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import ISichtung from "src/components/ISichtung";
import Zeile from "./Zeile.vue";

@Component({ components: { Zeile } })
export default class Sichtungsliste extends Vue {
  private ListItems: ISichtung[] = [];

  private refreshList(): void {
    let tmplist: ISichtung[] = [];
    fetch("/rest/sichtungen")
      .then(response => {
        if (!response.ok) {
          throw new Error("doof gelaufen");
        }
        return response.json();
      })
      .then(jsondata => {
        for (const uri of jsondata) {
          fetch(uri)
            .then(response => {
              if (!response.ok) {
                throw new Error("bleh");
              }
              return response.json();
            })
            .then(responsedata => {
              tmplist.push(responsedata);
            })
            .catch(error => {
              console.log(error);
            });
        }
      })
      .catch(error => {
        console.log(error);
      });
    this.ListItems = tmplist;
  }
  private get sichtungslistenitems(): ISichtung[] {
    return this.ListItems;
  }
}
</script>
<style scoped>
.table {
  width: 100%;
  margin: 0 auto;
}
table {
  border-collapse: collapse;
  width: 100%;
}

th, td {
  text-align: center;
  padding: 8px;
}

tr:nth-child(even){background-color: #f2f2f2}

th {
  background-color: #4CAF50;
  color: white;
}
tr:nth-child(even) {background-color: #f2f2f2;}

Button {
	-moz-box-shadow:inset 0px 1px 0px 0px #7a8eb9;
	-webkit-box-shadow:inset 0px 1px 0px 0px #7a8eb9;
	box-shadow:inset 0px 1px 0px 0px #7a8eb9;
	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #637aad), color-stop(1, #5972a7));
	background:-moz-linear-gradient(top, #637aad 5%, #5972a7 100%);
	background:-webkit-linear-gradient(top, #637aad 5%, #5972a7 100%);
	background:-o-linear-gradient(top, #637aad 5%, #5972a7 100%);
	background:-ms-linear-gradient(top, #637aad 5%, #5972a7 100%);
	background:linear-gradient(to bottom, #637aad 5%, #5972a7 100%);
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#637aad', endColorstr='#5972a7',GradientType=0);
	background-color:#637aad;
	border:1px solid #314179;
	display:inline-block;
	cursor:pointer;
	color:#ffffff;
	font-family:Arial;
	font-size:13px;
	font-weight:bold;
	padding:6px 12px;
	text-decoration:none;
}
Button:hover {
	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #5972a7), color-stop(1, #637aad));
	background:-moz-linear-gradient(top, #5972a7 5%, #637aad 100%);
	background:-webkit-linear-gradient(top, #5972a7 5%, #637aad 100%);
	background:-o-linear-gradient(top, #5972a7 5%, #637aad 100%);
	background:-ms-linear-gradient(top, #5972a7 5%, #637aad 100%);
	background:linear-gradient(to bottom, #5972a7 5%, #637aad 100%);
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#5972a7', endColorstr='#637aad',GradientType=0);
	background-color:#5972a7;
}
Button:active {
	position:relative;
	top:1px;
}

</style>

