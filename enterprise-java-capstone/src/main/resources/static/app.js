const API = "/api/appointments";
const rows = document.querySelector("#appointment-rows");
const notice = document.querySelector("#notice");
const dialog = document.querySelector("#appointment-dialog");
const form = document.querySelector("#appointment-form");

async function request(url, options = {}) {
  const response = await fetch(url, {
    headers: { "Content-Type": "application/json", ...(options.headers || {}) },
    ...options
  });
  if (!response.ok) {
    let message = `Request failed (${response.status})`;
    try {
      const body = await response.json();
      message = body.message || body.error || message;
      if (body.validationErrors) message += ": " + Object.values(body.validationErrors).join(", ");
    } catch (_) {}
    throw new Error(message);
  }
  if (response.status === 204) return null;
  return response.json();
}

function escapeHtml(value) {
  return String(value ?? "").replace(/[&<>"']/g, ch => ({
    "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"
  }[ch]));
}

function showNotice(message, isError = false) {
  notice.textContent = message;
  notice.style.color = isError ? "#bd4545" : "#27865b";
}

async function loadAppointments() {
  rows.innerHTML = '<tr><td colspan="6">Loading appointments…</td></tr>';
  try {
    const appointments = await request(API);
    document.querySelector("#total-count").textContent = appointments.length;
    document.querySelector("#scheduled-count").textContent = appointments.filter(a => a.status === "SCHEDULED").length;
    document.querySelector("#department-count").textContent = new Set(appointments.map(a => a.department)).size;
    if (!appointments.length) {
      rows.innerHTML = '<tr><td colspan="6">No appointments yet. Select “New appointment” to add one.</td></tr>';
      return;
    }
    rows.innerHTML = appointments.map(a => `<tr>
      <td>${escapeHtml(a.patientName)}</td><td>${escapeHtml(a.doctorName)}</td>
      <td>${escapeHtml(a.department)}</td><td>${escapeHtml(a.appointmentDate)}</td>
      <td><span class="status">${escapeHtml(a.status)}</span></td>
      <td><button class="delete" data-delete="${a.id}">Delete</button></td></tr>`).join("");
  } catch (error) {
    rows.innerHTML = '<tr><td colspan="6">Could not load appointments.</td></tr>';
    showNotice(error.message, true);
  }
}

document.querySelector("#open-form").addEventListener("click", () => dialog.showModal());
document.querySelector("#close-form").addEventListener("click", () => dialog.close());
document.querySelector("#cancel-form").addEventListener("click", () => dialog.close());
document.querySelector("#refresh").addEventListener("click", loadAppointments);

form.addEventListener("submit", async event => {
  event.preventDefault();
  const data = Object.fromEntries(new FormData(form).entries());
  try {
    await request(API, { method: "POST", body: JSON.stringify(data) });
    form.reset();
    dialog.close();
    showNotice("Appointment created successfully.");
    await loadAppointments();
  } catch (error) {
    showNotice(error.message, true);
  }
});

rows.addEventListener("click", async event => {
  const button = event.target.closest("[data-delete]");
  if (!button) return;
  if (!confirm("Delete this appointment?")) return;
  try {
    await request(`${API}/${button.dataset.delete}`, { method: "DELETE" });
    showNotice("Appointment deleted.");
    await loadAppointments();
  } catch (error) {
    showNotice(error.message, true);
  }
});

loadAppointments();
