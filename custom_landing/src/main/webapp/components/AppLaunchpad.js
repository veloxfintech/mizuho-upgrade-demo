// tag::LaunchpadComponent[]
VeloxVue.component('appLaunchpad', {
  template: `
    <div class="app-launchpad flex-row flex-1">
      <div class="sideMenu">
        <div class="launchpad-items" v-for="group, header in items.alphabets">
          <h2>{{ header }}</h2>
          <div class="launchpad-item flex-row gap ai-center" v-for="item of group" @click="open(item)">
            <span :class="['launchpad-item-icon', item.icon.Value]"></span>
            <div class="launchpad-item-name flex-1">{{ item.caption.Value }}&nbsp;&nbsp;</div>
            <span :class="['launchpad-item-favorite', 'fa-solid', 'fa-heart', {'isFavorite' : item.isFavorite.Value==='true' }]"
                  @click.stop="like(item)"></span>
          </div>
        </div>
      </div>
      <div class="launchpad-container flex-col flex-1 gap">
        <button class="toggle flex-row"
                @click="toggle()">
          <template v-if="showFavorites.Value === 'true'">
            <span class="material-icons-outlined">chevron_right</span>
            <span>All apps</span>
          </template>
          <template v-else>
            <span class="material-icons-outlined">chevron_left</span>
            <span>Back</span>
          </template>
        </button>
        <div v-if="showFavorites.Value === 'true'" class="favorites flex-1">
          <h1>Favorites</h1>
          <div class="launchpad-items flex-row">
            <div class="launchpad-item flex-col gap ai-center" v-for="item in items.favorites" @click="open(item)">
              <img v-if="item.imageUrl.Value !== null"
                   :src="item.imageUrl.Value"
                   alt="picture icon"
                   class="launchpad-item-icon">
              <div v-else :class="['launchpad-item-icon', item.icon.Value]"></div>
              <div class="launchpad-item-name">{{ item.caption.Value }}</div>
            </div>
          </div>
        </div>
        <div v-else class="grouped flex-1">
          <h1>All Apps</h1>
          <div class="flex-col" v-for="group, groupName in items.grouped">
            <h2>{{ groupName }}</h2>
            <div class="launchpad-items flex-row">
              <div class="launchpad-item flex-col gap ai-center" v-for="item of group" @click="open(item)">
                <img v-if="item.imageUrl.Value !== null"
                     :src="item.imageUrl.Value"
                     alt="picture icon"
                     class="launchpad-item-icon">
                <div v-else :class="['launchpad-item-icon', item.icon.Value]"></div>
                <div class="launchpad-item-name">{{ item.caption.Value }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  props: {
    items: {
      type: Object, required: true
    },
    showFavorites: {
      type: Object, required: true
    }
  },
  setup(props) {
    const {inject, ref, watchEffect} = Vue;
    const $vx = inject("$vx");

    const toggle = () => $vx.store.changeValue(props.showFavorites, props.showFavorites.Value !== 'true')
    const like = (item) => $vx.store.changeValue(item.isFavorite, item.isFavorite.Value !== 'true')
    const open = (item) => $vx.store.takeAction(item.open)

    const items = ref({});
    watchEffect(() => {
      const favorites = [];
      const grouped = {};
      const alphabets = {};
      const screens = [];

      for (const opt of props.items.Options) {
        const screenId = parseInt(opt.value.replaceAll(',', ''));
        if ($vx.store.screens[screenId] !== undefined) {
          const screen = $vx.store.screens[screenId].$controls;
          screens.push(screen);
        }
      }

      screens.sort((a, b) => {
        return a.caption.Value.localeCompare(b.caption.Value);
      })

      for (const screen of screens) {
        const caption = screen.caption.Value;
        const header = caption[0].toUpperCase();
        const group = screen.group.Value;

        if (!alphabets[header]) {
          alphabets[header] = [];
        }
        alphabets[header].push(screen)

        if (!grouped[group]) {
          grouped[group] = [];
        }
        grouped[group].push(screen);

        if (screen.isFavorite.Value === 'true') {
          favorites.push(screen);
        }
      }
      items.value = {
        favorites: favorites, grouped: grouped, alphabets: alphabets,
      };
    })

    return {
      toggle, like, open, items,
    }
  }
})
// end::LaunchpadComponent[]