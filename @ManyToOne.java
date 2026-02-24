@ManyToOne
@JoinColumn(name = "branch_id", nullable = false)
private BankBranch branch; // ✅ Correct type