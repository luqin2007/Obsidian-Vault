// fix: Tabs
function fixTabs(tab) {
    let buttons = tab.querySelectorAll(".tabs-nav > .tabs-nav-item-wrapper > .tabs-nav-item");
    let contents = tab.querySelectorAll(".tabs-contents > .tabs-content");
    let currentButton = tab.querySelector(".tabs-nav > .tabs-nav-item-wrapper > .tabs-nav-item-active");
    let currentContent = tab.querySelector(".tabs-contents > .tabs-content-active");

    for (let i = 0; i < buttons.length; i++) {
        let button = buttons[i];
        button.addEventListener("click", () => {
            currentButton.classList.remove("tabs-nav-item-active");
            currentContent.classList.remove("tabs-content-active");
            currentButton = button;
            currentContent = contents[i];
            currentButton.classList.add("tabs-nav-item-active");
            currentContent.classList.add("tabs-content-active");
        });
    }
}
document.querySelectorAll(".tabs-container").forEach(fixTabs);
