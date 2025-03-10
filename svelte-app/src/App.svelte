<script lang="ts">
  // import '@atlassian/aui/dist/aui/aui-prototyping.js';
  import FileTable from './lib/FileTable.svelte';

  let path: string = 'D:/';
</script>

<main class="container">
  <FileTable {path} />
</main>
