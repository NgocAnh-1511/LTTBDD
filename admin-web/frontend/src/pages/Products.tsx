import { Box, Typography, Paper, Alert } from '@mui/material'

export default function Products() {
  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Products Management
      </Typography>
      <Paper sx={{ p: 3, mt: 2 }}>
        <Alert severity="info">
          Products page - Coming soon. Data will be loaded from Firebase.
        </Alert>
      </Paper>
    </Box>
  )
}

