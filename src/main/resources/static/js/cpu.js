// Image preview functionality
document.getElementById('image').addEventListener('change', function(event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();

        reader.onload = function(e) {
            const imagePreview = document.getElementById('imagePreview');
            imagePreview.innerHTML = `<img src="${e.target.result}" alt="CPU Image Preview">`;
        };

        reader.readAsDataURL(file);
        document.getElementById('fileName').textContent = file.name;
    } else {
        const imagePreview = document.getElementById('imagePreview');
        imagePreview.innerHTML = `
            <div class="image-preview-placeholder">
                <i class="bi bi-image" style="font-size: 2rem;"></i>
                <p>No image selected</p>
            </div>`;
        document.getElementById('fileName').textContent = 'No file selected';
    }
});

// Automatically suggest threads based on cores
document.getElementById('cores').addEventListener('change', function() {
    const cores = parseInt(this.value);
    const threadsSelect = document.getElementById('threads');

    if (cores && !isNaN(cores)) {
        const suggestedThreads = cores * 2;

        for (let i = 0; i < threadsSelect.options.length; i++) {
            if (parseInt(threadsSelect.options[i].value) === suggestedThreads) {
                threadsSelect.selectedIndex = i;
                break;
            }
        }
    }
});

// Form validation
(function() {
    'use strict';

    var forms = document.querySelectorAll('.form-container');
    Array.from(forms).forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
})();

function confirmDelete() {
    return confirm('Are you sure you want to delete this CPU? This action cannot be undone.');
}

