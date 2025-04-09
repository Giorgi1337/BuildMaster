// Image preview functionality
document.getElementById('image')?.addEventListener('change', function(event) {
    const file = event.target.files[0];
    const imagePreview = document.getElementById('imagePreview');
    const fileNameDisplay = document.getElementById('fileName');

    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            imagePreview.innerHTML = `<img src="${e.target.result}" alt="Motherboard Image Preview" class="img-fluid">`;
        };
        reader.readAsDataURL(file);
        fileNameDisplay.textContent = file.name;
    } else {
        imagePreview.innerHTML = `
            <div class="image-preview-placeholder">
                <i class="bi bi-image" style="font-size: 2rem;"></i>
                <p>No image selected</p>
            </div>`;
        fileNameDisplay.textContent = 'No file selected';
    }
});

function updateChipsetOptions() {
    const socket = document.getElementById("socket").value;
    const chipsetSelect = document.getElementById("chipset");

    const chipsetCompatibility = {
        "AM4": ["B550", "X570"],
        "AM5": ["B650", "X670"],
        "LGA1200": ["Z490", "Z590"],
        "LGA1700": ["Z690"]
    };

    // Clear existing options
    chipsetSelect.innerHTML = '<option value="">Select Chipset</option>';

    if (socket && chipsetCompatibility[socket]) {
        chipsetCompatibility[socket].forEach(chipset => {
            const option = document.createElement("option");
            option.value = chipset;
            option.text = chipset;
            chipsetSelect.appendChild(option);
        });
    }
}

window.addEventListener('DOMContentLoaded', () => {
    updateChipsetOptions();

    // Restore previously selected chipset on edit
    const chipsetSelect = document.getElementById("chipset");
    const currentChipset = chipsetSelect.getAttribute("data-current-chipset");
    if (currentChipset) {
        const existingOption = Array.from(chipsetSelect.options).find(opt => opt.value === currentChipset);
        if (existingOption) existingOption.selected = true;
    }

    // Form validation
    const forms = document.querySelectorAll('.form-container');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
});

function confirmDelete() {
    return confirm('Are you sure you want to delete this motherboard? This action cannot be undone.');
}
