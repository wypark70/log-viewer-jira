<script lang="ts">
  import { onMount } from "svelte";
  import type { FileInfo } from "./types";

  import {
    getIconForFile,
    getIconForFolder,
    getIconForOpenFolder,
  } from "vscode-icons-js";

  export let path: string = "";
  let files: FileInfo[] = [];

  async function fetchFiles(path: string) {
    const response = await fetch(`/rest/log-viewer/1.0/files?path=${path}`);
    files = await response.json();
  }

  function handleFileClick(file: FileInfo) {
    if (file.directory) {
      path = `${file.absolutePath}`;
      fetchFiles(path);
    } else {
      // Handle file click if needed
    }
  }

  function formatSize(size: number): string {
    if (size === 0) {
      return '';
    } else if (size >= 1024 * 1024) {
      return (size / (1024 * 1024)).toFixed(2) + " MB";
    } else {
      return (size / 1024).toFixed(2) + " KB";
    }
  }

  function getFileIcon(file: FileInfo): string {
    if (file.directory) {
      return getIconForFolder(file.name) || "";
    }
    return getIconForFile(file.name) || "";
  }

  onMount(() => {
    fetchFiles(path);
  });
</script>

<div class="file-table">
  <h3>
    <button
      class="aui-button aui-button-link"
      on:click={() => fetchFiles(path)}
    >
      <img src="/icons/{getIconForOpenFolder(path) || ''}" style="width:1em;" alt="Folder" />
      {path || "Root"}
    </button>
  </h3>
  <table class="aui">
    <thead>
      <tr>
        <th id="fileInfo-name">Name</th>
        <th id="fileInfo-type">Type</th>
        <th id="fileInfo-size">Size</th>
        <th id="fileInfo-lastModified">Last Modified</th>
      </tr>
    </thead>
    <tbody>
      {#each files as file}
        <tr>
          <td headers="fileInfo-name">
            {#if file.directory}
            <button
              class="aui-button aui-button-link"
              on:click={() => handleFileClick(file)}
            >
              <img src="/icons/{getFileIcon(file)}" style="width:1em;" alt="Folder" />
              {file.name}
            </button>
            {:else}
            <img src="/icons/{getFileIcon(file)}" style="width:1em;" alt="Folder" />
            <a href="/rest/log-viewer/1.0/files/download?path={file.absolutePath}">{file.name}</a>
            {/if}

          </td>
          <td headers="fileInfo-type"
            >{file.directory ? "Directory" : "File"}</td
          >
          <td headers="fileInfo-size">{formatSize(file.size)}</td>
          <td headers="fileInfo-lastModified"
            >{new Date(file.lastModified).toLocaleString()}</td
          >
        </tr>
      {/each}
    </tbody>
  </table>
</div>
