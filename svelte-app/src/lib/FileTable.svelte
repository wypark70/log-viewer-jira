<script lang="ts">
  import { onMount } from "svelte";
  import type { FileInfo } from "./types";
  import mime from "mime";

  import {
    getIconForFile,
    getIconForFolder,
    getIconForOpenFolder,
  } from "vscode-icons-js";

  export let path: string = "";

  let files: FileInfo[] = [];
  let showModal = false;
  let fileName = "";
  let fileContent = "";
  let rootName = "D:";
  let currentPath = path || "D:/";
  
  async function fetchFiles(absolutePath: string) {
    currentPath = absolutePath;
    try {
      const response = await fetch(
        `/rest/log-viewer/1.0/files?path=${absolutePath}`
      );
      if (!response.ok) {
        throw new Error(`Error fetching files: ${response.statusText}`);
      }
      files = await response.json();
    } catch (error) {
      console.log((error as Error).message);
    }
  }

  async function fetchFileRead(absolutePath: string) {
    fileContent = "";
    try {
      const response = await fetch(
        `/rest/log-viewer/1.0/files/read?path=${absolutePath}&lines=500`
      );
      if (!response.ok) {
        throw new Error(`Error reading file: ${response.statusText}`);
      }
      fileName = absolutePath.split(/[\\/]/).pop() || "";
      fileContent = await response.text();
    } catch (error) {
      fileContent = (error as Error).message;
    }
  }

  async function handleFileClick(file: FileInfo) {
    if (file.directory) {
      fetchFiles(file.absolutePath);
    } else {
      fetchFileRead(file.absolutePath);
      showModal = true;
    }
  }

  function formatSize(size: number): string {
    if (size === 0) {
      return "";
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

  function getPathLinks(path: string): { name: string; path: string }[] {
    const parts = path.split(/[\\/]/);
    return parts.map((part, index) => ({
      name: part,
      path: index === 0 ? rootName + "/" : parts.slice(0, index + 1).join("/"),
    }));
  }

  onMount(() => {
    fetchFiles(path);
  });
</script>

<div class="file-table">
  <h2>
    {#each getPathLinks(currentPath) as { name, path }}
      <button
        class="aui-button aui-button-link"
        style="margin-left: 0"
        on:click={() => fetchFiles(path)}
      >
        {#if name.endsWith(rootName)}
          <img
            src="/icons/{getIconForOpenFolder(path) || ''}"
            style="width:1em;"
            alt="Folder"
          />
        {/if}
        {!name.endsWith(rootName) ? "/" + name: name}
      </button>
    {/each}
  </h2>
  <table class="aui">
    <thead>
      <tr>
        <th id="fileInfo-name">Name</th>
        <th id="fileInfo-mime">Mime</th>
        <th id="fileInfo-type">Type</th>
        <th id="fileInfo-size">Size</th>
        <th id="fileInfo-lastModified">Last Modified</th>
      </tr>
    </thead>
    <tbody>
      {#each files as file}
        <tr>
          <td headers="fileInfo-name">
            <button
              class="aui-button aui-button-link"
              on:click={() => handleFileClick(file)}
            >
              <img
                src="/icons/{getFileIcon(file)}"
                style="width:1em;"
                alt="Folder"
              />
              {file.name}
            </button>
          </td>
          <td headers="fileInfo-mime">{mime.getType(file.name)}</td>
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

{#if showModal}
  <div
    id="static-dialog"
    class="aui-dialog2 aui-dialog2-medium"
    role="dialog"
    aria-modal="true"
    tabindex="-1"
    aria-labelledby="static-dialog--heading"
    aria-describedby="static-dialog--description"
  >
    <header class="aui-dialog2-header">
      <h1 class="aui-dialog2-header-main" id="static-dialog--heading">
        {fileName}
      </h1>
    </header>
    <div class="aui-dialog2-content" id="static-dialog--description">
      <pre>{fileContent}</pre>
    </div>
    <footer class="aui-dialog2-footer">
      <div class="aui-dialog2-footer-actions">
        <button
          class="aui-button aui-button-link"
          on:click={() => (showModal = false)}>Close</button
        >
      </div>
      <div class="aui-dialog2-footer-hint"></div>
    </footer>
  </div>
{/if}

<style>
  .aui-dialog2 {
    position: fixed;
    top: 50%;
    left: 50%;
    width: 80%;
    height: 80%;
    transform: translate(-50%, -50%);
    z-index: 1000;
  }

  .file-table h2 {
    white-space: nowrap;
  }

  .file-table h2 button {
    white-space: none;
  }
</style>
