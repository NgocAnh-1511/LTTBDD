import { Box, Typography, Paper, Alert } from '@mui/material'

export default function Categories() {
  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Categories Management
      </Typography>
      <Paper sx={{ p: 3, mt: 2 }}>
        <Alert severity="info">
          Categories page - Coming soon. Data will be loaded from Firebase.
        </Alert>
      </Paper>
    </Box>
  )
}

