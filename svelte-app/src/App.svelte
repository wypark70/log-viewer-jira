<script lang="ts">
  import svelteLogo from './assets/svelte.svg';
  import viteLogo from '/vite.svg';
  import FileTable from './lib/FileTable.svelte'; // Import the new FileTable component
  import FileContent from './lib/FileContent.svelte'; // Import the new FileContent component
  import type { File } from './lib/types'; // Import the File interface

  let path: string = '';
  let selectedFilePath: string = '';
  let lines: number = 100;

  function handleFileClick(event: CustomEvent) {
    const file: File = event.detail;
    if (file.directory) {
      path = `${path}/${file.name}`;
    } else {
      selectedFilePath = file.name;
    }
  }
</script>

<main class="container">
  <div class="logos">
    <a href="https://vite.dev" target="_blank" rel="noreferrer">
      <img src={viteLogo} class="logo" alt="Vite Logo" />
    </a>
    <a href="https://svelte.dev" target="_blank" rel="noreferrer">
      <img src={svelteLogo} class="logo svelte" alt="Svelte Logo" />
    </a>
  </div>
  <h1>Vite + Svelte</h1>

  <FileTable {path} on:fileClick={handleFileClick} /> <!-- Use the FileTable component and pass the path -->

  {#if selectedFilePath}
    <FileContent {path} {lines} /> <!-- Use the FileContent component to display the file content -->
  {/if}

  <p>
    Check out <a href="https://github.com/sveltejs/kit#readme" target="_blank" rel="noreferrer">SvelteKit</a>, the official Svelte app framework powered by Vite!
  </p>

  <p class="read-the-docs">
    Click on the Vite and Svelte logos to learn more
  </p>
</main>

<style>
  .container {
    max-width: 960px;
    margin: 0 auto;
    padding: 16px;
  }
  .logos {
    display: flex;
    justify-content: center;
    gap: 16px;
  }
  .logo {
    height: 6em;
    padding: 1.5em;
    will-change: filter;
    transition: filter 300ms;
  }
  .logo:hover {
    filter: drop-shadow(0 0 2em #646cffaa);
  }
  .logo.svelte:hover {
    filter: drop-shadow(0 0 2em #ff3e00aa);
  }
  h1 {
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji";
    font-size: 2em;
    color: #24292e;
    text-align: center;
  }
  .read-the-docs {
    color: #888;
    text-align: center;
  }
</style>
